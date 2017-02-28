[![Build Status](https://travis-ci.org/axel3rd/files-archiver.svg?branch=master)](https://travis-ci.org/axel3rd/files-archiver)

# files-archiver
Archive files (e.g: photos albums, ...) in ZIPs, respecting directories.
Create/update ZIP only if more 3 files in directory.

Create binary distribution _files-archiver-x.y.z-bin.zip_ : ``mvn install``

Extract it and use it with this usage:

```
java -jar files-archiver-x.y.z.jar inputDirectory outputDirectory fileType1,fileType2 [patternForbidden1,patternForbiddenX]
```

__Sample__ for photos albums :

```
java -jar files-archiver-x.y.z.jar /inputDir/albums/ /outputDir/archives/ jpg,gif,avi,mov,mpeg,mp4 __thumb,__small
```

Test PR foo bar 2
