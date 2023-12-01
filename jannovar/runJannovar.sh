#!/bin/sh

#
# Run Jannavar Utility
#
# Usage:  runJannovar.sh [text]
#
# example:
# Col1a1<m1Btlr>
# chr11:94838000GC>AT
#
# split by "\t"
# chr11:94838000GC>AT     INTERGENIC_VARIANT,INTERGENIC_VARIANT   Gm11544:NM_001205037.1::,Gm11543:XM_975056.4::  .;.
#

cd `dirname $0` && . ../Configuration

${JAVA} -Xms2G -Xmx2G -jar ${MGD_JAVA_API}/jannovar/jannovar-cli-0.38.jar annotate-pos -d ${MGD_JAVA_API}/jannovar/refseq_108_mm10.ser --3-letter-amino-acids --show-all -c $1

