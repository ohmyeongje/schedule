# schedule

일정 관리 앱 API 설계하기
일정 등록	POST	/api/schedule	요청 body	등록 정보	200: 정상등록
일정 조회	GET	/api/schedule/{schedule}	요청 param	단건 응답 정보	200: 정상조회
일정 목록 조회	GET	/api/schedule	요청 param	다건 응답 정보	200: 정상조회
일정 수정	PUT	/api/schedule/{schedule}	요청 body	수정 정보	200: 정상수정
일정 삭제	DELETE	/api/schedule/{schedule}	요청 param	-	200: 정상삭제
