import sys
import json
from pymilvus import MilvusClient, model
import sys
import io

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

class MilvusSearcher:
    def __init__(self, db_path="./milvus.db", collection_name="prepare_experience"):
        self.client = MilvusClient(db_path)
        self.embedding_fn = model.DefaultEmbeddingFunction()
        self.collection_name = collection_name

    def search_text(self, question, limit=1, output_fields=["text"]):
        query_vectors = self.embedding_fn.encode_queries([question])
        res = self.client.search(
            collection_name=self.collection_name,
            data=query_vectors,
            limit=limit,
            output_fields=output_fields
        )
        return [item['entity']['text'] for item in res[0]] if res and res[0] else []

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print(json.dumps({"error": "Missing question or collectionName parameter"}))
        sys.exit(1)

    question = sys.argv[1]
    collection_name = sys.argv[2]

    searcher = MilvusSearcher(collection_name=collection_name)
    try:
        result = searcher.search_text(question)
        print(json.dumps(result, ensure_ascii=False))  # 加 ensure_ascii=False 支持中文
    except Exception as e:
        print(json.dumps({"error": str(e)}))
        sys.exit(1)
