#!/bin/bash

target_dir="./submit2/"
metafile="./metafile.meta"
filepath="./tree.tree"
inputfile="./stage3-15000000.data"
heapArr=(16)
buffArr=(4096)
blockArr=(500 1000 1500 2000)
#buffArr=(8192)
#blockArr=(2000)

for heap in ${heapArr[@]}
do
	for nblock in ${blockArr[@]}
	do
		for file in `ls $target_dir`
		do
			echo $file
			for buffersize in ${buffArr[@]}
			do
				`javac -encoding cp949 -cp .:../commons-lang3-3.7.jar:tinyse-2018.stage_3.build_1.jar:$target_dir$file Insert.java`
				sync && echo 3 > /proc/sys/vm/drop_caches
                                echo 3 > /proc/sys/vm/drop_caches
                                echo `java -cp .:../commons-lang3-3.7.jar:tinyse-2018.stage_3.build_1.jar:$target_dir$file -Xmx$heap"m" Insert $metafile $filepath $buffersize $nblock $file $inputfile`
				`javac -encoding cp949 -cp .:../commons-lang3-3.7.jar:tinyse-2018.stage_3.build_1.jar:$target_dir$file Search.java`
				sync && echo 3 > /proc/sys/vm/drop_caches
                                echo 3 > /proc/sys/vm/drop_caches
				echo `java -cp .:../commons-lang3-3.7.jar:tinyse-2018.stage_3.build_1.jar:$target_dir$file -Xmx$heap"m" Search $metafile $filepath $buffersize $nblock $file $inputfile`
				`rm $metafile`
				`rm $filepath`
				`rm Insert.class`
				`rm Search.class`
			done
		done
	done
done
