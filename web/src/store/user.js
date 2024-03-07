import $ from 'jquery'

export default {
    state: {
        id: "",
        username: "",
        photo: "",
        token: "",
        email: "",
        is_login: false,
        pulling_info: true,  // 是否正在从云端拉取信息
        role: "",
    },
    getters: {
    },
    mutations: {
        updatePhoto (state, photo) {
            state.photo = photo;
          },
        updateUsername (state, username) {
            state.username = username;
        },  // 更新用户名  未使用
        
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.email = user.email;
            state.photo = user.photo;
            state.is_login = user.is_login;
            state.rating = user.rating;
            state.role = user.role;
        },
        updateToken(state, token) {
            state.token = token;
        },
  
        logout(state) {
            state.id = "";
            state.username = "";
            state.email = "";
            state.photo = "";
            state.token = "";
            state.is_login = false;
            state.role = "";
        },
        updatePullingInfo(state, pulling_info) {
            state.pulling_info = pulling_info;
        }
    },
    actions: {
        login(context, data) {
            $.ajax({
                url: "http://127.0.0.1:3000/api/user/account/token/",
                type: "post",
                data: {
                    username: data.username,
                    password: data.password,
                },
                success(resp) {
                    if (resp.code === 200) {
                        localStorage.setItem("jwt_token", resp.data.token);
                        context.commit("updateToken", resp.data.token);
                        data.success(resp);
                    } else {
                        data.error(resp);
                    }
                },
                error(resp) {
                    data.error(resp);
                }
            });
        },

        getInfo(context, data) {
            $.ajax({
                url: "http://127.0.0.1:3000/api/user/account/info/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + context.state.token,
                },
                success(resp) {
                    if (resp.code === 200) {
                        context.commit("updateUser", {
                            ...resp.data,
                            is_login: true,
                        });
                        data.success(resp);
                        console.log(resp);
                    } else {
                        data.error(resp);
                    }
                },
                error(resp) {
                    data.error(resp);
                }
            })
        },
        logout(context) {
            localStorage.removeItem("jwt_token");
            context.commit("logout");
        }
    },
    modules: {
    }
}