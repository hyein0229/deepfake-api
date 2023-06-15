import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import App from "./App";
import Join from "./Join";
import Login from "./Login";
import Logout from "./Logout";
import Upload from "./Upload";
import WebEditor from "./WebEditor";
import VideoUpload from "./VideoUpload";

//const root = ReactDOM.createRoot(document.getElementById('root'));
ReactDOM.render(
    <BrowserRouter>
        <Routes>
            <Route path={"/"} element={<App />} />
            <Route path={"/join"} element={<Join />} />
            <Route path={"/login"} element={<Login/>} />
            <Route path={"/logout"} element={<Logout/>} />
            <Route path={"/upload"} element={<Upload/>} />
            <Route path={"/webEditor"} element={<WebEditor/>} />
            <Route path={"/VideoUpload"} element={<VideoUpload/>} />
        </Routes>
    </BrowserRouter>,
    document.getElementById('root')
);