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
import {useCookies} from "react-cookie";

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

    // 아이디, 비밀번호 저장
    const [Id, setId] = useState('');
    const [Password, setPassword] = useState('');
    const [cookies, setCookie, removeCookie] = useCookies();

    // 입력 상태 메세지 저장
    const [idMessage, setIdMessage] = useState('');
    const [passwordMessage, setPasswordMessage] = useState('');

    // 입력값 유효성 저장
    const [isId, setIsId] = useState(false);
    const [isPassword, setIsPassword] = useState(false);

    const navigate = useNavigate();

    // 이벤트 handler 함수
    const idHandler = (event) => {
        setId(event.currentTarget.value)
        if(event.currentTarget.value.length > 0){
            setIdMessage('');
            setIsId(true);
        }else{
            setIdMessage('아이디를 입력하세요.');
            setIsId(false);
        }
    };
    const passwordHandler = (event) => {
        setPassword(event.currentTarget.value)
        if(event.currentTarget.value.length > 0){
            setPasswordMessage('');
            setIsPassword(true);
        }else{
            setPasswordMessage('비밀번호를 입력하세요.');
            setIsPassword(false);
        }
    };

    const loginHandler = (event) => {
        // 버튼을 눌렀을 때 페이지가 리로드 되는것을 막음
        event.preventDefault();

        console.log('Id:', Id);
        console.log('Password:', Password);
        // 서버 API 연동
        axios({
            headers: {
                'Access-Control-Allow-Origin': 'http://localhost:9999'	// 서버 domain
            },
            method:"post",
            url:"/members/Login",
            withCredentials: true,
            data:{
                id:Id,
                password1:Password
            }
        })
            .then((response)=>{
                if(response.status === 200){ //로그인 성공
                    localStorage.setItem("memberId", Id)
                    localStorage.setItem("isLogin", true)
                    navigate("/");
                } else {
                    alert("로그인에 실패했습니다.")
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
                        로그인
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
                        </Grid><br/>
                        <Button color={"primary"} fullWidth onClick={loginHandler} variant="contained" disabled={!(isId && isPassword)}>Login</Button><br/><br/>
                    </Box>
                </Box>
                <br/><br/>
                <Copyright sx={{ mt: 5 }} />
            </Container>
        </ThemeProvider>
    );
}