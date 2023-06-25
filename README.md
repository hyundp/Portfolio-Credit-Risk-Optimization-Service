# Data-Structure-Team-Project

DS Team Project (22.10 ~ 22.12)

### 주제
Portfolio Credit-Risk Optimization 문제를 해결하여 사용자에게 서비스를 제공하자.

![image](https://user-images.githubusercontent.com/75602169/211878110-4f0018f3-72d2-4379-b652-39d4571f01ad.png)


| file name | language | summary | conclusion | time complexity | space complexity |
|:--:|:--:|:--:|:--:|:--:|:--:|
| data_preprocessing | python | 데이터 전처리 | 모든 데이터를 처리할 수 있지만 직관적이지 않고, 빠르지 않고, 공간 활용이 좋지 않다. | O(n) | O(n) |
| Strategy1 | java | 서비스 시간을 최소화 하는 전략 | 즉각적인 서비스가 가능하지만 공간이 많이 쓰여서 heap memory를 가용량을 증가시켜야한다. | O(n) | O(n) |
| Strategy2 | java | HashMap을 활용해 메모리 효율을 높이는 전략 | shallow copy로 인해 데이터가 변경되는 현상이 나타난다. | O(n) | O(n) |
| Strategy3 | java | JVM의 Garbage Collection 동작을 효율적이게 만드는 전략 | 효율적이긴 하지만 유의미한 차이는 발생하지 않았다. | O(n) | O(n) |
| Strategy4 | java | 서비스 시간과 메모리 용량간의 절충안 | 서비스 시간이 가장 느리지만, 메모리 효율은 가장 좋았다. | O(n) | O(n) |
| BigStructure | java | 모든 데이터를 메모리에 저장하는 구조체 | X | X | X |
| SmallStructure | java | 특정 데이터만을 메모리에 저장하는 구조체 | X | X | X |
