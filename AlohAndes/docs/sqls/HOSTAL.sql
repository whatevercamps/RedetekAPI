CREATE TABLE HOSTAL (
ID NUMBER NOT NULL,
COD_INTEND NUMBER NOT NULL, 
CAPACIDAD NUMBER NOT NULL,
APERTURA DATE NOT NULL,
CLAUSURA DATE NOT NULL,
CONSTRAINT PK_HOSTAL PRIMARY KEY (ID),
CONSTRAINT FK_HOSTAL_OPERADOR FOREIGN KEY (ID) REFERENCES OPERADORES(ID)
);


ALTER TABLE HOSTALES ADD(
CLAUSURA VARCHAR2(40 BYTE));

