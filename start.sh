export LANG=zh_CN.UTF-8
FILE_PATH=/home/hadoop/application/Dps-channel-routing
cd ${FILE_PATH}

MainClass=com.umpay.channelRouting.proxyservice.StartService
SERVICE_ID=online_Dps-channel-routing

APPCLASSPATH=
APPCLASSPATH=$APPCLASSPATH:.
APPCLASSPATH=$APPCLASSPATH:bin
APPCLASSPATH=$APPCLASSPATH:resource
for jarfile in `ls -1 lib/*.jar`
do
    APPCLASSPATH="$APPCLASSPATH:$jarfile"
done

for jarfile1 in `ls -1 lib/hbase098/*.jar`
do
    APPCLASSPATH="$APPCLASSPATH:$jarfile1"
done


pid=`ps -wwef|grep "Dflag=${SERVICE_ID}"|grep -v grep`

if [ -n "${pid}" ]
then
    echo "${SERVICE_ID} already start."
else
    nohup java -Xms500m -Xmx1000m -Xmn128m -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=70 -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:"./log/gc-server.log" -cp ${APPCLASSPATH} -Dflag=${SERVICE_ID} ${MainClass} > /dev/null 2>&1 &
    echo $! > server.pid
    echo "start success"
fi