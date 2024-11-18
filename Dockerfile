# 构建阶段
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app

# 复制 Maven 包和源代码
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src

# 构建应用
RUN ./mvnw clean package -DskipTests

# 运行阶段
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 复制构建产物
COPY --from=builder /app/target/*.jar app.jar

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