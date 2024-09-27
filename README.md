[![Test with Gradle](https://github.com/a-fujimt/shusshare/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/a-fujimt/shusshare/actions/workflows/gradle-build.yml)
[![codecov](https://codecov.io/gh/akichil-fj/shusshare/branch/main/graph/badge.svg?token=3GHLEMN1UK)](https://codecov.io/gh/akichil-fj/shusshare)

# Shusshare(出社share)
## What's this?
自分の出社状況をシェアできるアプリケーション。
今日誰が出社しているかを一目で確認することができます。 

## 動作要件
- Java 17+

## localでshusshareを起動するには
1-3までのステップを実行することで、shusshareをlocalで起動することができる。

1. プロファイル or 環境変数の追加
`application.yml`には、DB接続情報が含まれていないため、環境変数orプロファイルを追加する。

```
# 以下2つ
spring.datasource.username
spring.datasource.password
```

2. データベースとテーブルの作成

またデータベースとテーブル作成のために `source schema.sql` を行う必要がある。
```
MariaDB [(none)]> source src/main/resources/schema.sql;
```

3. サーバー起動
ステップ1と2を行った後に、`gradle run` を実行し、サーバーを起動。`gradle` はMacbookを使っている場合、 `brew install gradle` でインストール可能。

## 使用技術
- SpringBoot
- Mybatis
- Thymeleaf
