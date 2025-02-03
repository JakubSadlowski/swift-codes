# Getting Started

### Used Postgresql

#### How to install locally ?

* Download zip with binaries and extract on your computer
  https://www.enterprisedb.com/download-postgresql-binaries;
* Initialize database

```
  initdb.exe -D ../data –-username=admin –auth=trust
```

* Start db server

```
pg_ctl start -D../data
```

* You may use e.g. DBWeaver in order to connect into it.
  Use https://dbeaver.io/download/