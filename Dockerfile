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
#ENV SERVER_PORT=3324
#ENV SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/great-rule
#ENV SPRING_DATASOURCE_USERNAME=root
#ENV SPRING_DATASOURCE_PASSWORD=root
#ENV LOGIN_PASSWORD=111

# 创建启动脚本
COPY start.sh /app/
RUN chmod +x /app/start.sh

ENTRYPOINT ["/app/start.sh"]