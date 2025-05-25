import json
import re
from pymilvus import MilvusClient, model

# 初始化Milvus客户端和嵌入模型
client = MilvusClient("./milvus.db")
embedding_fn = model.DefaultEmbeddingFunction()

if client.has_collection(collection_name="select_experience"):
    client.drop_collection(collection_name="select_experience")
client.create_collection(
    collection_name="select_experience",
    dimension=768,  # The vectors we will use in this demo has 768 dimensions
)
with open("择校建议1.txt",  "r", encoding="utf-8") as file:
    data1 = file.read()
with  open("择校建议2.txt",  "r", encoding="utf-8") as file:
    data2 = file.read()

docs = [data1,  data2]
problem =["我是山东大学（威海校区）大三计算机专业的学生，我是江苏人，我想考本省的学校的研究生，专业不挑，不要和我原来的专业差别太大就行，有哪些可以推荐的？",
          "我是扬州大学计算机专业的学生，我想考985，211的研究生，请你推荐一些容易上岸的冷门宝藏院校，专业只要和计算机相关就行。"]
vectors = embedding_fn.encode_documents(problem)
print("Dim:", embedding_fn.dim, vectors[0].shape)  # Dim: 768 (768,)

data = [
    {"id": i, "vector": vectors[i], "text": docs[i]}
    for i in range(len(vectors))
]

print("Data has", len(data), "entities, each with fields: ", data[0].keys())
print("Vector dim:", len(data[0]["vector"]))

res = client.insert(collection_name="select_experience", data=data)

print(res)

