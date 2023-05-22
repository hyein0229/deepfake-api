import React, {useEffect, useState} from 'react';
import { Link } from "react-router-dom";
import {Button} from "@material-ui/core";
import {useNavigate} from "react-router-dom";

function App() {

    const navigate = useNavigate();

    const join = () => {
        navigate('/join')
    }

    const login = () => {
        navigate('/login')
    }

    const uploadPage = () => {
        navigate('/upload')
    }

    const logout = () => {
        navigate('/logout')
    }

    const goWebEditor = () => {
        navigate('/webEditor')
    }

    if(localStorage.getItem("memberId") == ""){
        return (
            <div align={"center"}>
                <Button type={"primary"} variant={"contained"} onClick={join}>
                    회원가입
                </Button>
                <Button type={"primary"} variant={"contained"} onClick={login}>
                    로그인
                </Button>
            </div>
        );
    }else{
        return (
            <div align={"center"}>
                <Button type={"primary"} variant={"contained"} onClick={uploadPage}>
                    이미지 업로드
                </Button>
                <Button type={"primary"} variant={"contained"} onClick={goWebEditor}>
                    웹에디터
                </Button>
                <Button type={"primary"} variant={"contained"} onClick={logout}>
                    로그아웃
                </Button>
            </div>
        );
    }
}

export default App;
