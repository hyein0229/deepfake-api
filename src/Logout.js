import * as React from 'react';
import axios from "axios";

export default function Logout() {

    const deleteCookie = (cookieName) => {
        document.cookie = cookieName + '=; expires=Thu, 01 Jan 1999 00:00:10 GMT;';
    }
    localStorage.setItem("memberName", "")
    localStorage.setItem("memberId", "")
    localStorage.setItem("isLogin", null)
    axios.post("/members/logout");
    deleteCookie('memberCode')
    window.location.href="/"
}