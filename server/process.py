#!/usr/bin/python

import os
import sys
import re

WORD_TOKENIZATION = re.compile("\W");

def removeHeader(contents):
    return contents.split("\n\n")[0]

def ignoreWord(word):
    if (word == ""): return True
    if (re.match("^[0-9_]",word)): return True
    return False

def generateWordList(f,wordFrequencies):
    contents = open(f, 'r').read()
    body = removeHeader(contents)
    words = WORD_TOKENIZATION.split(contents)

    for word in words:
        word = word.lower()

        if (ignoreWord(word)): continue #TODO expand list of ignored words

        if (word not in wordFrequencies):
            wordFrequencies[word] = 0;

        wordFrequencies[word] += 1;

def countFiles(corpus):
    count = 0
    for folder, subFolders, files in os.walk(corpus):
        count += files.__len__();
    return count;
        

def getWordFrequenciesFromCorpus(corpus):
    fileCount = countFiles(corpus)
    print "%d emails to process." % fileCount

    wordFrequencies = {}
    processedFiles = 0
    for folder, subFolders, files in os.walk(corpus):
        for f in files:
            processedFiles += 1
            if (processedFiles % 1000 == 0):
                print "Completed %d of %d emails" % (processedFiles, fileCount)
            path = folder + "/" + f
            generateWordList(path,wordFrequencies);
    return wordFrequencies

def frequenciesToSortedList(frequencies):
    wordList = frequencies.keys()
    return sorted(wordList)


corpus = sys.argv[1]
print "Processing directory: %s" % corpus

wordFrequencies = getWordFrequenciesFromCorpus(corpus);
wordList = frequenciesToSortedList(wordFrequencies)

print "Found %d unique words." % ( wordList.__len__())

destination = "../processedCorpus"

depth = 2
pbeg = None
fd = None
for word in wordList:
    beg = word[:depth]

    path = destination
    for c in beg:
        path = os.path.join(path,c)
    path += ".txt"

    if (beg != pbeg):
        print
        if (fd): fd.close()

        try:
            os.makedirs(os.path.dirname(path))
        except:
            pass

        fd = open(path,"w+")

    fd.write(word+"\n")

    pbeg = beg

if (fd): fd.close()
