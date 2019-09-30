import requests
import os
import sys

if len(sys.argv) != 3:
	print("Usage: python query-relations.py <resources-dir> <query-terms file> <output file>")
	sys.exit()

RES_DIR = sys.argv[0]

f = open(os.path.join(RES_DIR, sys.argv[1]), "r", encoding="utf8")
fw = open(os.path.join(RES_DIR, sys.argv[2]), "w", encoding="utf8")

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
	
f.close()
fw.close()
