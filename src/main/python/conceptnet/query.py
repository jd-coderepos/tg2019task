import requests
import json
import os

url = "https://concept.research.microsoft.com/api/Concept/ScoreByProb"

RES_DIR = "../../data/resources"

f = open(os.path.join(RES_DIR, "querytermsforconceptnet.txt"), "r", encoding="utf8")
fw = open(os.path.join(RES_DIR, "new-concepts.txt"), "w", encoding="utf8")

for x in f:
	x = x.strip()
	querystring = {"instance":x,"topK":"100"}

	headers = {
		'cache-control': "no-cache",
		'Postman-Token': "2a0d0897-7b68-4255-b18d-f232f79a75d1"
		}

	response = requests.request("GET", url, headers=headers, params=querystring)

	result = json.loads(response.text)
	
	string = ""
	for k, v in result.items():
		string += str(k)+"\t"
	fw.write(x+"\t"+string+"\n")

f.close()
fw.close()