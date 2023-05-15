import * as React from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { createTheme, ThemeProvider } from '@material-ui/core/styles';
import {useNavigate} from "react-router-dom";
import {useState} from "react";
import axios from "axios";

function Copyright(props) {
    return (
        <Typography variant="body2" color="text.secondary" align="center" {...props}>
            {'Copyright © '}
            <Link color="inherit" href="https://mui.com/">
                Your Website
            </Link>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}

const theme = createTheme();

export default function Join() {

    // 아이디, 비밀번호, 이메일, 이름 상태값 저장
    const [Id, setId] = useState('');
    const [Password, setPassword] = useState('');
    const [ConfirmedPassword, setConfirmedPassword] = useState('');
    const [Name, setName] = useState('');
    const [Email, setEmail] = useState('');

    // 입력 상태 메세지 저장
    const [idMessage, setIdMessage] = useState('');
    const [passwordMessage, setPasswordMessage] = useState('');
    const [confirmedPasswordMessage, setConfirmedPasswordMessage] = useState('');
    const [nameMessage, setNameMessage] = useState('');
    const [emailMessage, setEmailMessage] = useState('');

    // 입력값 유효성 저장
    const [isId, setIsId] = useState(false);
    const [isName, setIsName] = useState(false);
    const [isEmail, setIsEmail] = useState(false);
    const [isPassword, setIsPassword] = useState(false);
    const [isPasswordConfirm, setIsPasswordConfirm] = useState(false);

    const navigate = useNavigate();

    // 이벤트 handler 함수
    const idHandler = (event) => {
        setId(event.currentTarget.value)
        if(event.currentTarget.value.length < 5 || event.currentTarget.value.length > 20){
            setIdMessage('5~20자로 입력하세요.');
            setIsId(false);
        }else{
            setIdMessage('');
            setIsId(true);
        }
    };
    const passwordHandler = (event) => {
        const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{5,25}$/;
        setPassword(event.currentTarget.value)

        // 비밀번호 양식 검사
        if (!passwordRegex.test(event.currentTarget.value)) {
            setPasswordMessage('숫자/영문자/특수문자 조합으로 5~25자 입력해주세요!');
            setIsPassword(false);
        } else {
            setPasswordMessage('안전한 비밀번호입니다:)');
            setIsPassword(true);
        }

        // 비밀번호가 수정되었으므로 비밀번호 재확인
        if(isPasswordConfirm){
            setConfirmedPasswordMessage('비밀번호를 재확인해주세요!');
            setIsPasswordConfirm(false);
        }
    };
    const confirmedPasswordHandler = (event) => {
        setConfirmedPassword(event.currentTarget.value)
        // 비밀번호가 일치하는지 검사
        if(event.currentTarget.value !== Password){
            setConfirmedPasswordMessage('비밀번호가 일치하지 않습니다.');
            setIsPasswordConfirm(false);
        }else{
            setConfirmedPasswordMessage('비밀번호가 일치합니다.');
            setIsPasswordConfirm(true);
        }
    };

    const nameHandler = (event) => {
        setName(event.currentTarget.value)
        // 이름 글자 수 검사
        if(event.currentTarget.value.length < 1 || event.currentTarget.value.length > 9){
            setNameMessage('1글자 이상 9글자 이하로 입력하세요.');
            setIsName(false);
        }else{
            setNameMessage('');
            setIsName(true);
        }
    };
    const emailHandler = (event) => {
        const emailRegex = /([\w-.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
        setEmail(event.currentTarget.value);
        // 이메일 양식 검사
        if(!emailRegex.test(event.currentTarget.value)){
            setEmailMessage('이메일 형식이 맞지 않습니다.');
            setIsEmail(false);
        }else{
            setEmailMessage('');
            setIsEmail(true);
        }
    };

    const joinHandler = (event) => {
        // 버튼을 눌렀을 때 페이지가 리로드 되는것을 막음
        event.preventDefault();

        console.log('Id', Id);
        console.log('Password', Password);
        // 서버 API 연동
        axios({
            method:"post",
            url:"/members/join",
            data:{
                id:Id,
                name:Name,
                password1:Password,
                password2:ConfirmedPassword,
                email: Email
            }
        })
            .then((response)=>{
                if(response.status === 200){ //로그인 성공
                    navigate("/");
                } else {
                    alert("회원가입에 실패했습니다.")
                }
            })
            .catch((error) => {
            })
    }

    return (
        <ThemeProvider theme={theme}>
            <Container component="main" maxWidth="xs">
                <CssBaseline />
                <Box
                    sx={{
                        marginTop: 8,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}
                >
                    <br/><br/>
                    <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
                        <LockOutlinedIcon />
                    </Avatar>
                    <br/>
                    <Typography component="h1" variant="h4">
                        회원가입
                    </Typography>
                    <Box component="form" noValidate sx={{ mt: 3 }}>
                        <Grid container fullwidth spacing={2}>
                            <Grid item xs={12}>
                                <TextField
                                    required
                                    fullWidth
                                    id="outlined-basic"
                                    label="ID"
                                    variant="outlined"
                                    onChange={idHandler}
                                />
                                {Id.length > 0 && <Typography color="secondary">{idMessage}</Typography>}
                            </Grid>
                            <Grid item xs={12} >
                                <TextField
                                    required
                                    fullWidth
                                    id="outlined-basic"
                                    label="PASSWORD"
                                    variant="outlined"
                                    onChange={passwordHandler}
                                />
                                {Password.length > 0 && (
                                    <Typography color={isPassword ? "primary" : "secondary"}>{passwordMessage}</Typography>
                                )}
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    required
                                    fullWidth
                                    id="outlined-basic"
                                    label="PASSWORD CHECK"
                                    variant="outlined"
                                    onChange={confirmedPasswordHandler}/>
                                {ConfirmedPassword.length > 0 && (
                                    <Typography color={isPasswordConfirm ? "primary" : "secondary"}>{confirmedPasswordMessage}</Typography>
                                )}
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    required
                                    fullWidth
                                    id="outlined-basic"
                                    label="NAME"
                                    variant="outlined"
                                    onChange={nameHandler}/>
                                {Name.length > 0 && <Typography color="secondary">{nameMessage}</Typography>}
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    required
                                    fullWidth
                                    id="outlined-basic"
                                    label="EMAIL"
                                    variant="outlined"
                                    onChange={emailHandler}/>
                                {Email.length > 0 && <Typography color="secondary">{emailMessage}</Typography>}
                            </Grid>
                        </Grid><br/>
                        <Button color={"primary"} fullWidth onClick={joinHandler} variant="contained" disabled={!(isId && isName && isEmail && isPassword && isPasswordConfirm)}>Join</Button><br/><br/>
                        <Grid container justifyContent="flex-end">
                            <Grid item>
                                <Link href={"/login"} variant="body2">
                                    Already have an account? Sign in
                                </Link>
                            </Grid>
                        </Grid>
                    </Box>
                </Box>
                <br/><br/>
                <Copyright sx={{ mt: 5 }} />
            </Container>
        </ThemeProvider>
    );
}