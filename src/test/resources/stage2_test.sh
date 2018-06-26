#!/bin/bash

target_dir="./stage2/submit/"
heapArr=(32)
buffArr=(4096)
blockArr=(1000)
#buffArr=(8192)
#blockArr=(2000)

for file in `ls $target_dir`
do
	for heap in ${heapArr[@]}
	do
		for nblock in ${blockArr[@]}
		do
			echo $file
			rr=$nblock
			rr+=" "
			for buffersize in ${buffArr[@]}
			do
				`javac -cp .:commons-lang3-3.7.jar:tinyse-2018.stage_2.build_1.jar:$target_dir$file Main.java`
				rr+=`java -cp .:commons-lang3-3.7.jar:tinyse-2018.stage_2.build_1.jar:$target_dir$file -Xmx$heap"m" Main $buffersize $nblock`
				`rm -r tmp`
				`rm sort-10000000.data`
				`mkdir tmp`
				`rm Main.class`
				rr+=" "
			done
			`echo $rr >> ./result/$heap"m"/$file.out`
		done
	done
done
