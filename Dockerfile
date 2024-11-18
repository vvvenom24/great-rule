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
    -XX:+UseContainerSupport \
    -Dfile.encoding=UTF-8"

ENTRYPOINT java $JAVA_OPTS -jar app.jar