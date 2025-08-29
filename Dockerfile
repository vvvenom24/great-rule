FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/"$TZ" /etc/localtime && \
    echo "$TZ" > /etc/timezone && \
    # 创建目录
    mkdir -p /app/logs /app/heapdump && \
    chmod 777 /app/heapdump && \
    chmod 777 /app/logs

# 复制构建产物和构建信息文件
COPY db/great-rule.db /app/db/great-rule.db
ARG JAR_FILE=target/great-rule-1.0.0.jar
COPY ${JAR_FILE} app.jar

# JVM 配置
ENV JAVA_OPTS="\
    --enable-preview \
    -XX:+UseZGC \
    -XX:+ZGenerational \
    -Xmx1024m \
    -Xms256m \
    -XX:+UseStringDeduplication \
    -XX:StringDeduplicationAgeThreshold=3 \
    -XX:+UseCompressedOops \
    -XX:+UseCompressedClassPointers \
    -XX:MaxGCPauseMillis=100 \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/app/heapdump/java_pid_%p.hprof \
    -XX:+ExitOnOutOfMemoryError \
    -Xlog:gc*=info:file=/app/heapdump/gc-%t.log:time,uptime,level,tags:filecount=5,filesize=100m \
    -Dfile.encoding=UTF-8 \
    -Duser.timezone=Asia/Shanghai \
    -Dlogging.file.path=/app/logs"

# 在 ENTRYPOINT 之前添加健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:2332/raw/rule/a || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]