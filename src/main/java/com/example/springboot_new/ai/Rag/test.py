from pymilvus import MilvusClient
from pymilvus import model

client = MilvusClient("/home/constina/IdeaProject/springboot_new/src/main/java/com/example/springboot_new/ai/Rag/milvus.db")##包含prepare_experience,select_experience两个表

embedding_fn = model.DefaultEmbeddingFunction()
query_vectors = embedding_fn.encode_queries(["考研数学怎么准备?"])

res = client.search(
    collection_name="prepare_experience",  # target collection
    data=query_vectors,  # query vectors
    limit=50,  # number of returned entities
    output_fields=["text", "subject"],  # specifies fields to be returned
)

print(res[0])
# for i in range(len(res[0])):
#     print(res[0][i].id)
print(len(res[0]))

# from pymilvus import MilvusClient
# from pymilvus import model
#
# client = MilvusClient("/home/constina/IdeaProject/springboot_new/src/main/java/com/example/springboot_new/ai/Rag/milvus.db")##包含prepare_experience,select_experience两个表
#
# embedding_fn = model.DefaultEmbeddingFunction()
# query_vectors = embedding_fn.encode_queries(["我是山东大学（威海校区）大三计算机专业的学生，我是江苏人，我想考本省的学校的研究生，专业不挑，不要和我原来的专业差别太大就行，有哪些可以推荐的？"])
#
# res = client.search(
#     collection_name="select_experience",  # target collection
#     data=query_vectors,  # query vectors
#     limit=50,  # number of returned entities
#     output_fields=["text"],  # specifies fields to be returned
# )
#
# print(res[0])
# # for i in range(len(res[0])):
# #     print(res[0][i].id)
# print(len(res[0]))