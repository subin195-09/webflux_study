# webflux_study
Spring WebFlux 에 대해 학습한 내용입니다.

### 실행방법

🌈 java 설치되어 있어야 합니다.

```bash
git clone https://github.com/subin195-09/webflux_study.git

cd webflux_study

./gradlew build

cd ./build/libs/

java -jar fluxdemo-0.0.1-SNAPSHOT.jar
```

### API List

- **GET** /fluxstream : Flux 형태로 데이터를 1초마다 하나씩 보내는 것이다. log에서 subscriber가 어떻게 동작하는지 알 수 있습니다.
- **GET** /customer : /fluxstream과 비슷하게 동작하는데, customer 테이블의 데이터를 보내줍니다.
- **GET** /customer/{id} : 특정 customer의 정보를 보내줍니다.
- **GET** /customer/sse : server send event 방식으로 서버에서 customer에 새로운 데이터가 생기면 받아옵니다.
- **POST** /customer : 새로운 customer를 생성해줍니다.


기본적인 subscriber의 onNext, onComplete의 동작을 보고 싶다면 위의 3개의 API로 요청을 보낸 후 log를 살펴보면 됩니다.

onSubscribe → request → onNext → onComplete

![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/2bb62975-090b-4f38-b286-32e5579d8d1e/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220915%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220915T123551Z&X-Amz-Expires=86400&X-Amz-Signature=852b8707d169a4cb7fbe42c13d1039525bf8b9ebc1b7c1b020598893c8dc7407&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

WebFlux의 비동기, non-blocking 방식을 보고 싶다면 “[http://localhost:8080/customer/sse](http://localhost:8080/customer/sse)” 에 접속하여 터미널에 아래의 명령어를 입력해보면 됩니다.

```bash
curl -X POST http://localhost:8080/customer
```

그러면 브라우저 창에 실시간으로 추가된 데이터를 받는게 보입니다.

![화면 기록 2022-09-15 오후 8.12.10.mov](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/97cbb9da-177e-47e1-86dd-27e35c84edd4/2022-09-15-81210-1.gif?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220915%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220915T124202Z&X-Amz-Expires=86400&X-Amz-Signature=79ccb2203e287f6fca94affd656e97b3bc8614816db37821a18d380024ec9122&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%222022-09-15-81210-1.gif%22&x-id=GetObject)
