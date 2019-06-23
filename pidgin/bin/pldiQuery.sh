#time pldiQuery.sh | tee query$(date +"%Y.%m.%d.%H.%M.%S").txt

classpath="$PIDGIN/classes"
classpath="$classpath:$PIDGIN/lib/java-cup-11a.jar"
classpath="$classpath:$PIDGIN/lib/jetty-all.jar"
classpath="$classpath:$PIDGIN/lib/servlet-api.jar"
classpath="$classpath:$PIDGIN/lib/JSON-java.jar"
classpath="$classpath:$PIDGIN/lib/commons-lang3-3.3.2.jar"
classpath="$classpath:$PIDGIN/lib/junit-4.10.jar"

folder="$ACCRUE_BYTECODE/tests/"
queryfolder="$PIDGIN/queries/pldi2015/" 
        
tax=$folder"pdg_test.programs.tax.TaxFreeMain.json.gz"
tax1=$queryfolder"tax_bytecode1.ql"
tax2=$queryfolder"tax_bytecode2.ql"
tax3=$queryfolder"tax_bytecode3.ql"
        
freecs=$folder"pdg_freecs.Driver.json.gz"
freecs1=$queryfolder"freecs1_bytecode.ql"
freecs2=$queryfolder"freecs2_bytecode.ql"
        
upm="$ACCRUE_BYTECODE/tests/pdg_com._17od.upm.Driver.json.gz"
upm1=$queryfolder"upm1_bytecode.ql"
upm2=$queryfolder"upm2_bytecode.ql"
upm3=$queryfolder"upm3_bytecode.ql"

cms=$folder"pdg_cms.Driver.json.gz"
cms1=$queryfolder"cms1_bytecode.ql"
cms2=$queryfolder"cms2_bytecode.ql"

tomcat1=$folder"pdg_tomcat.Driver_CVE_2010_1157.json.gz"
tomcat1q=$queryfolder"CVE_2010_1157.ql"

tomcat2=$folder"pdg_tomcat.Driver_CVE_2011_0013.json.gz"
tomcat2q=$queryfolder"CVE-2011-0013_post_fix.ql"

tomcat3=$folder"pdg_tomcat.Driver_CVE_2011_2204.json.gz"
tomcat3q=$queryfolder"CVE_2011_2204.ql"

tomcat4=$folder"pdg_tomcat.Driver_CVE_2014_0033.json.gz"
tomcat4q=$queryfolder"CVE_2014_0033.ql"

i=1

#qfile=$upm1
#gfile=$upm
#echo $gfile
#echo ""
#echo $qfile
#for i in $(eval echo {1..$i}); do
#sudo purge
#java -Xmx25G -Xms25G -classpath $classpath test.NewTestDriver $gfile $qfile
#done

qfile=$upm2
gfile=$upm
#echo $gfile
echo ""
echo $qfile
for i in $(eval echo {1..$i}); do
sudo purge
java -Xmx25G -Xms25G -classpath $classpath test.NewTestDriver $gfile $qfile
done

qfile=$upm3
gfile=$upm
#echo $gfile
echo ""
echo $qfile
for i in $(eval echo {1..$i}); do
sudo purge
java -Xmx25G -Xms25G -classpath $classpath test.NewTestDriver $gfile $qfile
done
 
qfile=$tax1
gfile=$tax
#echo $gfile
echo ""
echo $qfile
for i in $(eval echo {1..$i}); do
sudo purge
java -Xmx25G -Xms25G -classpath $classpath test.NewTestDriver $gfile $qfile
done

qfile=$tax2
gfile=$tax
#echo $gfile
echo ""
echo $qfile
for i in $(eval echo {1..$i}); do
sudo purge
java -Xmx25G -Xms25G -classpath $classpath test.NewTestDriver $gfile $qfile
done

qfile=$tax3
gfile=$tax
#echo $gfile
echo ""
echo $qfile
for i in $(eval echo {1..$i}); do
sudo purge
java -Xmx25G -Xms25G -classpath $classpath test.NewTestDriver $gfile $qfile
done

qfile=$freecs1
gfile=$freecs
#echo $gfile
echo ""
echo $qfile
for i in $(eval echo {1..$i}); do
sudo purge
java -Xmx25G -Xms25G -classpath $classpath test.NewTestDriver $gfile $qfile
done

qfile=$freecs2
gfile=$freecs
#echo $gfile
echo ""
echo $qfile
for i in $(eval echo {1..$i}); do
sudo purge
java -Xmx25G -Xms25G -classpath $classpath test.NewTestDriver $gfile $qfile
done

qfile=$cms1
gfile=$cms
#echo $gfile
echo ""
echo $qfile
for i in $(eval echo {1..$i}); do
sudo purge
java -Xmx25G -Xms25G -classpath $classpath test.NewTestDriver $gfile $qfile
done

qfile=$cms2
gfile=$cms
#echo $gfile
echo ""
echo $qfile
for i in $(eval echo {1..$i}); do
sudo purge
java -Xmx25G -Xms25G -classpath $classpath test.NewTestDriver $gfile $qfile
done

qfile=$tomcat1q
gfile=$tomcat1
#echo $gfile
echo ""
echo $qfile
for i in $(eval echo {1..$i}); do
sudo purge
java -Xmx25G -Xms25G -classpath $classpath test.NewTestDriver $gfile $qfile
done

qfile=$tomcat2q
gfile=$tomcat2
#echo $gfile
echo ""
echo $qfile
for i in $(eval echo {1..$i}); do
sudo purge
java -Xmx25G -Xms25G -classpath $classpath test.NewTestDriver $gfile $qfile
done

qfile=$tomcat3q
gfile=$tomcat3
#echo $gfile
echo ""
echo $qfile
for i in $(eval echo {1..$i}); do
sudo purge
java -Xmx25G -Xms25G -classpath $classpath test.NewTestDriver $gfile $qfile
done

qfile=$tomcat4q
gfile=$tomcat4
#echo $gfile
echo ""
echo $qfile
for i in $(eval echo {1..$i}); do
sudo purge
java -Xmx25G -Xms25G -classpath $classpath test.NewTestDriver $gfile $qfile
done
