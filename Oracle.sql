WITH objectfolder AS (
SELECT 
    SYS_CONNECT_BY_PATH(OBJECT_NAME, '\') as FOLDER,
    level, 
    OBJECT_ID,
    OBJECT_TYPE,
    PARENT_ID, 
    OBJECT_NAME 
FROM  META.DSSMDOBJINFO
START WITH PARENT_ID = '00000000000000000000000000000000'
CONNECT BY NOCYCLE PRIOR  OBJECT_ID = PARENT_ID and OBJECT_TYPE in (8) and object_state = 0
)

SELECT
    obinf.PROJECT_ID,
    obinf.OBJECT_TYPE,
    obinf.OBJECT_NAME,
    COUNT(DISTINCT obinf.OBJECT_ID)
FROM
    META.DSSMDOBJINFO obinf
    JOIN objectfolder obf on obf.OBJECT_ID = obinf.PARENT_ID
WHERE 
    obinf.HIDDEN = 0
    and obinf.OBJECT_TYPE in (3,4,7,12,55)
    and obinf.OBJECT_STATE = 0
    and obinf.PROJECT_ID = 'D25AABC1427C7F989F7D89BC26FAF1F6'
    and LOWER(obf.FOLDER) not like ('%\profiles%')
    and LOWER(obf.FOLDER) not like ('%\object templates%')

GROUP BY 
    obinf.PROJECT_ID, 
    obinf.OBJECT_NAME, 
    obinf.OBJECT_TYPE
HAVING COUNT(DISTINCT obinf.OBJECT_ID)>1
ORDER BY 
    obinf.PROJECT_ID,
    obinf.OBJECT_TYPE,
    obinf.OBJECT_NAME;
