<template>
    <div>
        <el-form :model="form" :rules="rules" ref="loginForm" label-width="120px">
            <el-form-item label="이메일" prop="email" required>{{ form.email }}
            </el-form-item>
            <el-form-item label="이전 비밀번호" prop="oldPassword" required>
                <el-input v-model="form.oldPassword" type="password" @keyup="checkPasswordAvailability"
                          placeholder="이전 비밀번호"></el-input>
            </el-form-item>
            <el-form-item label="수정 비밀번호" prop="password" required>
                <el-input v-model="form.password" type="password" placeholder="새로운 비밀번호"></el-input>
            </el-form-item>
            <el-form-item label="비밀번호 확인" prop="confirmPassword" required>
                <el-input v-model="form.confirmPassword" type="password" placeholder="비밀번호 다시입력"></el-input>
            </el-form-item>
            <el-form-item label="닉네임" prop="nickname" required>
                <el-input v-model="form.nickname" placeholder="변경할 닉네임"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button @click="checkNameAvailability" type="primary">중복 검증</el-button>
            </el-form-item>
            <el-form-item>
                <el-button type="warning" @click="editForm">수정</el-button>
                <el-button type="danger" @click="deleteForm">탈퇴</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue';
import axios from 'axios';
import router from "@/router";
import Cookies from "vue-cookies";
import {showCustomAlert} from "@/main";

const form = ref({
    email: '',
    oldPassword: '',
    authPassword: '',
    password: '',
    confirmPassword: '',
    nickname: '',
})
const rules = {
    oldPassword: [
        {required: true, message: '기존 비밀번호를 입력해주세요', trigger: 'blur'},
        {validator: validateConfirmOldPassword, trigger: 'blur'},
        {min: 6, message: '비밀번호는 최소 6자 이상', trigger: 'blur'},
    ],
    password: [
        {required: true, message: '새로운 비밀번호를 입력해주세요', trigger: 'blur'},
        {min: 6, message: '비밀번호는 최소 6자 이상이어야 합니다', trigger: 'blur'},
    ],
    confirmPassword: [
        {required: true, message: '비밀번호 확인을 입력해주세요', trigger: 'blur'},
        {validator: validateConfirmPassword, trigger: 'blur'},
    ],
    nickname: [
        {required: false, message: '변경할 닉네임을 입력해주세요', trigger: 'blur'},
    ],
}

function validateConfirmOldPassword(rule, value, callback) {
    if (form.value.authPassword !== true) {
        callback(new Error('기존 비밀번호가 일치하지 않습니다'));
    } else {
        callback();
    }
}

function validateConfirmPassword(rule, value, callback) {
    if (value !== form.value.password) {
        callback(new Error('비밀번호가 일치하지 않습니다'));
    } else {
        callback();
    }
}

const checkPasswordAvailability = () => {
    const token = Cookies.get('accessToken');
    axios
        .post('/api/auth/user/password', {
            password: form.value.oldPassword,
        }, {
            headers: {
                Authorization: token,
            },
        })
        .then((response) => {
            const valid = response.data.authResult;
            console.log(valid)
            const showCustomAlert = (message) => {
                const customAlert = document.createElement('div');
                customAlert.classList.add('custom-alert');
                customAlert.textContent = message;

                document.body.appendChild(customAlert);

                setTimeout(() => {
                    customAlert.remove();
                }, 2500);
            }
            form.value.authPassword = valid;

            if (valid) {
                // 기존 비밀번호가 일치할 때의 동작
                showCustomAlert('기존 비밀번호와 일치합니다.');
            } else {
                showCustomAlert('기존 비밀번호와 일치하지 않습니다.');
            }
        })
        .catch((error) => {
            if (error.response) {
                const errorMessage = error.response.data.message;
                showCustomAlert(`${errorMessage}`)
            }
        });
}
const checkNameAvailability = () => {
    console.log(form.value.email)
    axios
        .post('/api/auth/signup/name', {
            name: form.value.nickname,
            email: form.value.email
        })
        .then((response) => {
            const valid = response.data.authResult;
            console.log(valid)
            const name = response.data.name;
            const showCustomAlert = (message) => {
                const customAlert = document.createElement('div');
                customAlert.classList.add('custom-alert');
                customAlert.textContent = message;

                document.body.appendChild(customAlert);

                setTimeout(() => {
                    customAlert.remove();
                }, 2500);
            }

            if (valid) {
                showCustomAlert('사용 가능한 닉네임입니다.');
            } /*else if (name == form.value.nickname) {
                showCustomAlert('사용 가능한 닉네임입니다.');*/
             else {
                showCustomAlert('중복된 닉네임입니다.');
            }
        })
        .catch((error) => {
            if (error.response) {
                const errorMessage = error.response.data.message;
                showCustomAlert(`${errorMessage}`)
            }
        });
};

const editForm = () => {
    const token = Cookies.get('accessToken');
    console.log(loginFormRef.value)
    if (loginFormRef.value) {
        axios
            .patch('/api/user', {
                password: form.value.password,
                name: form.value.nickname,
            }, {
                headers: {
                    Authorization: token,
                },
            })
            .then(() => {
                alert("수정이 완료되었습니다.")
                router.replace({name: "home"});
            })
            .catch((error) => {
                if (error.response) {
                    const errorMessage = error.response.data.message;
                    showCustomAlert(`${errorMessage}`)
                }
            });
    }
}
const deleteForm = () => {
    const token = Cookies.get('accessToken');
    console.log(loginFormRef.value)
    if (loginFormRef.value) {
        axios
            .delete('/api/user', {
                password: form.value.oldPassword,
            }, {
                headers: {
                    Authorization: token,
                },
            })
            .then(() => {
                alert("삭제가 완료되었습니다.")
                router.replace({name: "home"});
            })
            .catch((error) => {
                if (error.response) {
                    const errorMessage = error.response.data.message;
                    showCustomAlert(`${errorMessage}`)
                }
            });
    }
}
const loginFormRef = ref(null);
const loginForm = ref(null)
onMounted(() => {
    loginFormRef.value = loginForm;
    const token = Cookies.get('accessToken');
    axios.post(`/api/auth/user/auth`,
        {},
        {
            headers: {
                Authorization: token
            }
        }).then((response) => {
        form.value.email = response.data.email;
        form.value.nickname = response.data.name;
    }).catch((error) => {
        if (error.response) {
            const errorMessage = error.response.data.message;
            showCustomAlert(`${errorMessage}`)
        }
    })
})
</script>
<style scoped>
.container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100vh;
}

.form {
    width: 400px;
}

.button {
    margin-top: 20px;
    text-align: center;
}

.error-message {
    color: red;
}

.error-message {
    color: blue;
}

.custom-alert {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    padding: 20px;
    background-color: #3C689F;
    color: #fff;
    text-align: center;
    font-size: 18px;
    z-index: 9999;
}
</style>
