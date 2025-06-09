import json
import re
from pymilvus import MilvusClient, model

# 初始化Milvus客户端和嵌入模型
client = MilvusClient("/home/constina/IdeaProject/springboot_new/src/main/java/com/example/springboot_new/ai/Rag/milvus.db")
embedding_fn = model.DefaultEmbeddingFunction()

# 数据清洗管道
def clean_content(content: str) -> str:
    """多步骤内容清洗"""
    content = re.sub(r"点击展开，查看完整图片", "", content)
    content = re.sub(r"\n{3,}", "\n\n", content)
    return content.strip()

# 学科检测增强版
SUBJECT_KEYWORDS = {
    "政治": ["政治", "肖秀荣", "徐涛", "肖四", "背诵手册"],
    "英语": ["英语", "阅读", "作文", "翻译", "背单词"],
    "数学": ["数学", "高数", "线代", "概率论", "张宇"],
    "计算机408": ["408", "数据结构", "操作系统", "计算机网络", "计算机组成"]
}

def detect_subject(content: str) -> str:
    """基于关键词权重的学科检测"""
    content = content.lower()
    scores = {k: 0 for k in SUBJECT_KEYWORDS}
    for subject, keywords in SUBJECT_KEYWORDS.items():
        for kw in keywords:
            if kw in content:
                scores[subject] += 1
    max_subject = max(scores, key=scores.get)
    return max_subject if scores[max_subject] > 0 else "综合经验"

# 处理流程
with open("9620593520_大家要自信，什么时候都不要妄自菲薄.json", "r", encoding="utf-8") as f:
    data = json.load(f)

processed_data = []
seen_hashes = set()

# 获取当前最大ID
current_max_id = 4
collection_name = "prepare_experience"
# if client.has_collection(collection_name):
#     res = client.query(
#         collection_name=collection_name,
#         output_fields=["id"],
#         limit=1,
#         order_by=[("id", "desc")]
#     )
#     if res:
#         current_max_id = res[0]["id"]
#         print(f"当前最大ID：{current_max_id}")

# 处理新数据时生成连续ID
start_id = current_max_id + 1

for post in data["lz_posts"]:
    cleaned = clean_content(post["content"])
    if len(cleaned) < 50:
        continue

    content_hash = hash(cleaned)
    if content_hash in seen_hashes:
        continue
    seen_hashes.add(content_hash)

    # 分配新ID
    new_id = start_id + len(processed_data)

    entity = {
        "id": new_id,
        "vector": None,
        "text": cleaned,
        "title": data["title"],
        "post_hash": content_hash
    }
    processed_data.append(entity)

# # 如果需要重建集合
# if client.has_collection(collection_name):
#     client.drop_collection(collection_name)

# 创建集合时指定维度和主键
client.create_collection(
    collection_name=collection_name,
    dimension=768,  # 必须与嵌入维度一致
    primary_field_name="id",
    metric_type="IP",  # 内积相似度
    auto_id=False  # 手动指定ID
)

# 创建索引（修复参数格式）
index_params = {
    "field_name": "vector",
    "index_type": "IVF_FLAT",
    "metric_type": "IP",
    "params": {"nlist": 1024}
}

# 加载集合到内存
client.load_collection(collection_name)

# 批量插入（含向量生成）
batch_size = 50
total_inserted = 0

for i in range(total_inserted, len(processed_data), batch_size):
    batch = processed_data[i:i + batch_size]
    # 综合文本、学科和标题生成嵌入输入
    composite_texts = [f"{item['title']} || {item['subject']} || {item['text']}" for item in batch]
    vectors = embedding_fn.encode_documents(composite_texts)

    # 填充向量并验证
    for item, vec in zip(batch, vectors):
        item['vector'] = vec.tolist()
        assert len(item['vector']) == 768, '向量维度错误'

    # 插入数据（自动匹配字段）
    res = client.insert(collection_name, data=batch)
    total_inserted += len(batch)
    print(f"已插入批次：{i // batch_size + 1}/{(len(processed_data) - 1) // batch_size + 1}")

print(f"成功存储{total_inserted}条经验数据到集合{collection_name}")
