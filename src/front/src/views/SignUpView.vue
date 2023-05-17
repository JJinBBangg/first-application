<template>
    <div>
        <el-form :model="form" :rules="rules" ref="loginForm" label-width="120px">
            <el-form-item label="이메일" prop="email" required>
                <el-input v-model="form.email" placeholder="이메일을 입력해주세요"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button @click="checkEmailAvailability" type="primary">중복 검증</el-button>
            </el-form-item>
            <el-form-item label="비밀번호" prop="password" required>
                <el-input v-model="form.password" type="password" placeholder="비밀번호를 입력해주세요"></el-input>
            </el-form-item>
            <el-form-item label="비밀번호 확인" prop="confirmPassword" required>
                <el-input v-model="form.confirmPassword" type="password" placeholder="비밀번호를 다시 입력해주세요"></el-input>
            </el-form-item>
            <el-form-item label="닉네임" prop="nickname" required>
                <el-input v-model="form.nickname" placeholder="닉네임을 입력해주세요"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button @click="checkNameAvailability" type="primary">중복 검증</el-button>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="submitForm">가입 완료</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import {computed, ref} from 'vue';
import axios from 'axios';
import router from "@/router";
import store from "@/stores/store";
import {showCustomAlert} from "@/main";

export default {
    data() {
        return {
            form: {
                email: '',
                password: '',
                confirmPassword: '',
                nickname: '',
            },
            rules: {
                email: [
                    {required: true, message: '이메일을 입력해주세요', trigger: 'blur'},
                    {type: 'email', message: '유효한 이메일 형식이 아닙니다', trigger: 'blur'},
                ],
                password: [
                    {required: true, message: '비밀번호를 입력해주세요', trigger: 'blur'},
                    {min: 6, message: '비밀번호는 최소 6자 이상이어야 합니다', trigger: 'blur'},
                ],
                confirmPassword: [
                    {required: true, message: '비밀번호 확인을 입력해주세요', trigger: 'blur'},
                    {validator: this.validateConfirmPassword, trigger: 'blur'},
                ],
                nickname: [
                    {required: true, message: '닉네임을 입력해주세요', trigger: 'blur'},
                ],
            },
        };
    },
    methods: {
        validateConfirmPassword(rule, value, callback) {
            if (value !== this.form.password) {
                callback(new Error('비밀번호가 일치하지 않습니다'));
            } else {
                callback();
            }
        },

        checkEmailAvailability() {
            axios
                .post('/api/auth/signup/email', {
                    email: this.form.email,
                })
                .then((response) => {
                    const valid = response.data.authResult;
                    const showCustomAlert=(message)=> {
                        const customAlert = document.createElement('div');
                        customAlert.classList.add('custom-alert');
                        customAlert.textContent = message;

                        document.body.appendChild(customAlert);

                        setTimeout(() => {
                            customAlert.remove();
                        }, 2500);
                    }

                    if (valid) {
                        showCustomAlert('사용 가능한 이메일입니다.');
                    } else {
                        showCustomAlert('중복된 이메일입니다.');
                    }
                })
                .catch((error) => {
                    if (error.response) {
                        const errorCode = error.response.data.code;
                        const errorMessage = error.response.data.message;
                        alert(`Error ${errorCode}: ${errorMessage}`);
                    }
                });


        },
        checkNameAvailability() {
            axios
                .post('/api/auth/signup/name', {
                    name: this.form.nickname,
                })
                .then((response) => {
                    const valid = response.data.authResult;
                    const showCustomAlert=(message)=> {
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
                    } else {
                        showCustomAlert('중복된 닉네임입니다.');
                    }
                })
                .catch((error) => {
                    if (error.response) {
                        const errorMessage = error.response.data.message;
                        showCustomAlert(`${errorMessage}`)
                    }
                });


        },
        submitForm() {
            this.$refs.loginForm.validate((valid) => {
                if (valid) {
                    axios
                        .post('/api/user', {
                            email: this.form.email,
                            password: this.form.password,
                            name: this.form.nickname,
                        })
                        .then(() => {
                            alert("가입이 완료되었습니다.")
                            router.replace({name: "login"});
                        })
                        .catch((error) => {
                            if (error.response) {
                                const errorMessage = error.response.data.message;
                                showCustomAlert(`${errorMessage}`)
                            }
                        });
                }
            });
        },
    },
};
</script>
<style>
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
