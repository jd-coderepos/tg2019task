# tg2019task

### Data preprocessing

As query data for ConceptNet and Wiktionary, create a text file with a list of all the non-stop words in the dataset of questions, answers, and explanation sentences. 

1. ConceptNet

   a. For ConceptNet concepts
   
   The script [query-concepts.py](/src/main/python/conceptnet/query-concepts.py) extracts associated concepts for each one listed in the file created above.

   b. For ConceptNet relations
   
   The script [query-relations.py](/src/main/python/conceptnet/query-relations.py) extracts associated relations for each one listed in the file created above.

2. Wiktionary

   a. For Wiktionary categories
   
   The script [query-term-categories.py](/src/main/python/wiki/query-term-categories.py) extracts associated concepts for each one listed in the file created above.

   b. For Wiktionary page titles of content matched pages with queried terms
   
   The script [query-text.py](/src/main/python/wiki/query-text.py) extracts associated relations for each one listed in the file created above.

3. FrameNet
   
   For FrameNet features, we use [Open-Sesame](https://github.com/swabhs/open-sesame) and [Mateplus](https://github.com/microth/mateplus).
   
#### Note: The ConceptNet, Wiktionary, and FrameNet data used in the shared task is made available in [data/resources](/data/resources) folder.

### Usage

To replicate the shared task features as is, we make available a pre-compiled `jar` file: [tg2019task.jar](tg2019task.jar)

To use it, download the data directory 

`java `

