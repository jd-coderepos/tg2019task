import requests
import os

#RES_DIR = "../../data/resources/conceptnet"
RES_DIR = "../../data/resources"

f = open(os.path.join(RES_DIR, "querytermsforconceptnet.txt"), "r", encoding="utf8")
#f = open(os.path.join(RES_DIR, "new-words.txt"), "r", encoding="utf8")
#f = open(os.path.join(RES_DIR, "new-wordlemmas.txt"), "r", encoding="utf8")
#fw = open(os.path.join(RES_DIR, "new-word-triples.txt"), "w", encoding="utf8")
#fw = open(os.path.join(RES_DIR, "new-wordlemma-triples.txt"), "w", encoding="utf8")
fw = open(os.path.join(RES_DIR, "new-wordtriples.txt"), "w", encoding="utf8")

for x in f:
	x = x.strip()
	
	r = requests.get('http://api.conceptnet.io/c/en/'+x)
	
	if (r.status_code != 200):
		continue
	
	obj = r.json()	
	
	triples_set = set()
	relations = {}
	
	for edge in obj['edges']:
		
		if 'language' not in edge['start'].keys() or edge['start']['language'] != 'en' or 'language' not in edge['end'].keys() or edge['end']['language'] != 'en':
			continue
		
		triples_set.add(edge['start']['label'] +"/"+ edge['rel']['label'] +"/"+ edge['end']['label'])
		
		x = x.replace("_", " ")
		
		if edge['rel']['label'] not in relations:
			if edge['start']['label'] == x:
				relations[edge['rel']['label']] = set()
				relations[edge['rel']['label']].add(edge['end']['label'])
			elif edge['end']['label'] == x:
				relations[edge['rel']['label']] = set()
				relations[edge['rel']['label']].add(edge['start']['label'])
		elif edge['rel']['label'] in relations:
			if edge['start']['label'] == x:
				relations[edge['rel']['label']].add(edge['end']['label'])
			elif edge['end']['label'] == x:
				relations[edge['rel']['label']].add(edge['start']['label'])
		
	str = ""
	for triples in triples_set:
		str = str+'\t'+triples
	str = str.strip()
	fw.write(x+"\t"+str+"\n")	

'''	
	for rel in relations:
		#fw_rel = open(os.path.join(RES_DIR, "wordlemma-"+rel+".txt"), "a", encoding="utf8")
		fw_rel = open(os.path.join(RES_DIR, rel+".txt"), "a", encoding="utf8")
		
		terms = relations[rel]
		
		rel_str = ""		
		for term in terms:
			rel_str = rel_str+'\t'+term
		
		rel_str = rel_str.strip()
		fw_rel.write(x+"\t"+rel_str+"\n")
		fw_rel.close()
'''	
	
f.close()
fw.close()
