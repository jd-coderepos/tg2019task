import requests
import os

RES_DIR = "../../data/resources/conceptnet"

#f = open(os.path.join(RES_DIR, "wordpairs.txt"), "r", encoding="utf8")
f = open(os.path.join(RES_DIR, "wordlemmapairs.txt"), "r", encoding="utf8")
#fw = open(os.path.join(RES_DIR, "wordpairs-relations.txt"), "w", encoding="utf8")
fw = open(os.path.join(RES_DIR, "wordlemmapairs-relations.txt"), "w", encoding="utf8")

for x in f:
	x = x.strip()
	terms = x.split("\t")	
	
	r = requests.get('http://api.conceptnet.io/query?node=/c/en/'+terms[0]+'&other=/c/en/'+terms[1])
	if (r.status_code != 200):
		continue
	
	obj = r.json()

	word1 = terms[0].replace("_", " ")
	word2 = terms[1].replace("_", " ")
		
	relations = set()
	for edge in obj['edges']:
	
		if 'language' not in edge['start'].keys() or edge['start']['language'] != 'en' or 'language' not in edge['end'].keys() or edge['end']['language'] != 'en':
			continue	
	
		relations.add(edge['rel']['label'])
	
	if len(relations) == 0:
		continue
	
	str = ""
	for relation in relations:	
		str = str+"\t"+relation
		
	str = str.strip()
	fw.write(word1+"\t"+word2+"\t"+str+"\n")
		
f.close()
fw.close()
