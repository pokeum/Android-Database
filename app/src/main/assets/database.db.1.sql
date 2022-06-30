CREATE TABLE people (
    id INT NOT NULL PRIMARY KEY,
    first_name CHAR(32),
    last_name CHAR(32)
);


CREATE TABLE device (
    model TEXT NOT NULL,
    nickname TEXT,
    display_size_inches REAL
);

-- device 테이블에 새로운 칼럼 추가
ALTER TABLE device ADD COLUMN memory_md REAL;

-- device 테이블 삭제
DROP TABLE device;

-- model 인덱스 생성
CREATE INDEX idx_device_model ON device(model);
-- model 인덱스 삭제
DROP INDEX idx_device_model;

-- device_name 뷰 생성
CREATE VIEW device_name AS SELECT model, nickname FROM device;
-- device_name 뷰 삭제
DROP VIEW device_name;



