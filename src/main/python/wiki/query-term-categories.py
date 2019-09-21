import requests
import os

url = "https://en.wiktionary.org/w/api.php"

RES_DIR = "../../data/resources"

f = open(os.path.join(RES_DIR, "querytermsforconceptnet.txt"), "r", encoding="utf8")
fw = open(os.path.join(RES_DIR, "new-wiki-concept-categories.txt"), "w", encoding="utf8")


for x in f:
	x = x.strip()
	#x = x.split('\t')[0]
	querystring = {"action":"query","prop":"categories","titles":x,"cllimit":"500","format":"json"}

	headers = {
		'User-Agent': "PostmanRuntime/7.15.0",
		'Accept': "*/*",
		'Cache-Control': "no-cache",
		'Postman-Token': "dddfed44-1505-40b1-85fe-6ce7e76d57c3,dace7b5f-e682-459c-afa6-89242ce736cd",
		'Host': "en.wiktionary.org",
		'cookie': "WMF-Last-Access-Global=27-Jun-2019; GeoIP=DE:NI:Barsinghausen:52.30:9.45:v4; WMF-Last-Access=27-Jun-2019",
		'accept-encoding': "gzip, deflate",
		'Connection': "keep-alive",
		'cache-control': "no-cache"
		}

	response = requests.request("GET", url, headers=headers, params=querystring).json()

	str = ""
	for k,v in response["query"]["pages"].items():
		if 'categories' not in v:
			continue
		i = len(v["categories"])-1
		end = i-10
		if end < 0:
			end = 0
		for j in range(i,end,-1):
			str = str + "\t" + v['categories'][j]['title'].replace("Category:","")
		str = str.strip()
	fw.write(x+"\t"+str+"\n")
	
f.close()
fw.close()
