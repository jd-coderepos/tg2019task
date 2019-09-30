import requests
import json
import os
import sys

if len(sys.argv) != 3:
	print("Usage: python query-concepts.py <resources-dir> <query-terms file> <output file>")
	sys.exit()

url = "https://concept.research.microsoft.com/api/Concept/ScoreByProb"

RES_DIR = sys.argv[0]

f = open(os.path.join(RES_DIR, sys.argv[1]), "r", encoding="utf8")
fw = open(os.path.join(RES_DIR, sys.argv[2]), "w", encoding="utf8")

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
