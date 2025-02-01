package fun.club.api.post;


public class ApiPostUrlConstants {
    public static final String BASE_API_URL = "/v1/api";

    // User
    public static final String USERS_BASE_URL = BASE_API_URL + "/users";
    public static final String USERS_SIGN_UP = BASE_API_URL + "/sign-up";
    public static final String USERS_LOGIN = BASE_API_URL + "/login";
    public static final String USERS_UPDATE_PROFILE = USERS_BASE_URL + "/profile";
    public static final String USERS_UPDATE_IMAGE = USERS_BASE_URL + "/{userId}/image";
    public static final String USERS_FIND_BY_ID = USERS_BASE_URL + "/{userId}";
    public static final String USERS_FIND_BY_USERNAME = USERS_BASE_URL + "/usernames";
    public static final String USERS_FIND_LOGIN_USER = USERS_BASE_URL + "/userInfo";
    public static final String USERS_DELETE_USER = USERS_BASE_URL;


    // NoticeBoard
    public static final String NOTICE_BOARDS_BASE_URL = BASE_API_URL + "/noticeBoards";
    public static final String NOTICE_BOARDS_FIND_ALL = NOTICE_BOARDS_BASE_URL;
    public static final String NOTICE_BOARDS_FIND_BY_USER = NOTICE_BOARDS_BASE_URL + "/writer/{userId}";
    public static final String NOTICE_BOARDS_FIND_BY_TITLE = NOTICE_BOARDS_BASE_URL + "/title";
    public static final String NOTICE_BOARDS_ADD_POST = NOTICE_BOARDS_BASE_URL + "/admins";
    public static final String NOTICE_BOARDS_UPDATE_POST = NOTICE_BOARDS_BASE_URL + "/admins/{boardId}";
    public static final String NOTICE_BOARDS_DELETE_POST = NOTICE_BOARDS_BASE_URL + "/admins/{boardId}";
    public static final String NOTICE_BOARDS_FIND_BY_ID = NOTICE_BOARDS_BASE_URL + "/{boardId}";


    // FreeBoard
    public static final String FREE_BOARDS_BASE_URL = BASE_API_URL + "/freeBoards";
    public static final String FREE_BOARDS_FIND_ALL = FREE_BOARDS_BASE_URL;
    public static final String FREE_BOARDS_FIND_BY_USER = FREE_BOARDS_BASE_URL + "/{userId}";
    public static final String FREE_BOARDS_FIND_BY_TITLE = FREE_BOARDS_BASE_URL + "/title";
    public static final String FREE_BOARDS_ADD_POST = FREE_BOARDS_BASE_URL;
    public static final String FREE_BOARDS_UPDATE_POST = FREE_BOARDS_BASE_URL + "/{boardId}";
    public static final String FREE_BOARDS_DELETE_POST = FREE_BOARDS_BASE_URL + "/{boardId}";
    public static final String FREE_BOARDS_FIND_BY_ID = FREE_BOARDS_BASE_URL + "/{boardId}";


    // Comment
    public static final String COMMENTS_BASE_URL = BASE_API_URL + "/comments";
    public static final String COMMENTS_ADD_POST = COMMENTS_BASE_URL +"/{boardId}";
    public static final String COMMENTS_UPDATE_POST = COMMENTS_BASE_URL;
    public static final String COMMENTS_DELETE_POST = COMMENTS_BASE_URL + "/{boardId}/{commentId}";
    public static final String COMMENTS_GET_COUNT = COMMENTS_BASE_URL + "/{boardId}/count";
}


