#!/bin/csh -f

#
# Run Jannavar Utility
#
# Usage:  runJannovar.csh [text]
#
# example:  runJannovar.csh "chrX:106092104G>A"
#
# chr[allele chromosome]:[curated genomic start][curated genomic reference allele]>[curated genomic variant allele]
# which is built by the pwi/variant/VariantController.js/copyHGVS()
#
# example:
# Col1a1<m1Btlr>
# chr11:94838000GC>AT
#

cd `dirname $0` && source ../Configuration

${JAVA} -Xms2G -Xmx2G -jar ${MGD_JAVA_API}/jannovar/jannovar-cli-0.38.jar annotate-pos -d ${MGD_JAVA_API}/jannovar/refseq_108_mm10.ser --3-letter-amino-acids --show-all -c $1

