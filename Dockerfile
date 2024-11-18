# 构建阶段
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app

# 安装 git
RUN apt-get update && apt-get install -y git

# 复制 Maven 包和源代码
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src
COPY .git ./.git

# 生成构建信息文件
RUN echo "Branch: $(git rev-parse --abbrev-ref HEAD)" > build.data && \
    echo "Commit: $(git rev-parse HEAD)" >> build.data && \
    echo "Author: $(git log -1 --pretty=format:'%an <%ae>')" >> build.data && \
    echo "Date:   $(git log -1 --pretty=format:'%ad')" >> build.data && \
    echo "$(git log -1 --pretty=format:'%s')" >> build.data

# 构建应用
RUN ./mvnw clean package -DskipTests

# 运行阶段
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 创建目录
RUN mkdir -p /app/logs /app/heapdump && \
    chmod 777 /app/heapdump && \
    chmod 777 /app/logs

# 复制构建产物和构建信息文件
COPY --from=builder /app/target/*.jar app.jar
COPY --from=builder /app/build.data ./build.data

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
    -Dlogging.file.path=/app/logs"

ENTRYPOINT java $JAVA_OPTS -Dlogging.file.path=/app/logs -jar app.jar