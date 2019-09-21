import requests
import os

url = "https://en.wiktionary.org/w/api.php"

RES_DIR = "../../data/resources"

f = open(os.path.join(RES_DIR, "querytermsforconceptnet.txt"), "r", encoding="utf8")
fw = open(os.path.join(RES_DIR, "new-wiki-concept-search-titles.txt"), "w", encoding="utf8")

for x in f:
	x = x.strip()
	#x = x.split('\t')[0]
	querystring = {"action":"query","list":"search","srwhat":"text","sroffset":"0","srlimit":"200","srsearch":x,"srprop":"sectiontitle","format":"json"}

	headers = {
		'User-Agent': "PostmanRuntime/7.15.0",
		'Accept': "*/*",
		'Cache-Control': "no-cache",
		'Postman-Token': "9f47564d-2040-45bc-9e90-38908685f60e,a1ed92ca-3fe4-4a7a-94d6-ad710b2395db",
		'Host': "en.wiktionary.org",
		'cookie': "WMF-Last-Access-Global=27-Jun-2019; GeoIP=DE:NI:Barsinghausen:52.30:9.45:v4; WMF-Last-Access=27-Jun-2019",
		'accept-encoding': "gzip, deflate",
		'Connection': "keep-alive",
		'cache-control': "no-cache"
		}

	response = requests.request("GET", url, headers=headers, params=querystring).json()

	titles = set()
	i = len(response["query"]["search"])
	for j in range(i):
		titles.add(response["query"]["search"][j]["title"])
	
	str = ""
	for s in titles:
		str = str+"\t"+s
	str = str.strip()
	fw.write(x+"\t"+str+"\n")
	
f.close()
fw.close()
