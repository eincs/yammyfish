# Yammy Fish

![ScreenShot](https://raw.githubusercontent.com/eincs/yammyfish/master/docs/screenshot.png)

물고기가 되어서 다른 물고기들을 잡아먹으며 성장하는 간단한 게임이며, 자바로 구현되어 있습니다.
2008년 텀프로젝트로 만든 것으로 코드 퀄리티는 정말... 누가봐도 구립니다.

## 실행 방법

본 프로젝트는  Java로 작성되어 있으므로 [JDK]를 설치하셔야하며, [Maven]을 통해 빌드하므로 Maven도 설치해야합니다.
`mvn package`하면 실행가능한 jar파일을  `build`폴더 내부에 생성합니다. 해당 jar파일을 실행시키면 게임을 할 수 있습니다.
혹은 `mvn exec:java` 명령어를 이용하면 빌드 후 실행까지 시켜주므로 이 명령어를 이용하셔도 됩니다.

## 게임 설명

1P : 방향키,
2P : wsad

물고기를 조정하며 자신보다 큰 물고기는 피하고, 자신보다 작은 물고기에겐 접근하여 먹습니다.
물고기들을 먹으면 크기가 커집니다.

[JDK]: http://www.oracle.com/technetwork/java/javase/downloads
[Maven]: http://maven.apache.org/

