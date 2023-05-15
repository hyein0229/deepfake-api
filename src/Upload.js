import * as React from 'react';
import { useState } from 'react';
import defaultimg from './logo192.png';
import axios from "axios";
import {useNavigate} from "react-router-dom";
import {Button} from "@material-ui/core";

export default function Upload() {
    const [imageSrc, setImageSrc] = useState(null);
    const [file, setFile] = useState(null)
    const navigate = useNavigate();

    let inputRef;

    const preview = (e) => {
        const file = e.target.files[0];
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = () => {
            setImageSrc(reader.result);
            setFile(file)
        }
    }

    const sendToServer = () => {
        const formData = new FormData();
        formData.append('file', file);

        axios({
            method:"post",
            url:"/upload",
            data: formData
        })
            .then((response)=>{
                if(response.status === 200){
                    alert("파일 업로드 성공!")
                    navigate("/");
                } else {
                    alert("서버에 파일 업로드하는 것을 실패했습니다.")
                }
            })
            .catch((error) => {
            })

    }

    const deleteImage = () => {
        setImageSrc(defaultimg);
    }

    return (
        <div align={"center"}>
            <input
                accept="image/*"
                type="file"
                ref={refParam => inputRef = refParam}
                onChange={e => preview(e)}
                style={{display: "none"}}
            />
            <div>
                <img src={imageSrc ? imageSrc : defaultimg}/>
            </div>

            <div>
                <Button type={"primary"} variant={"contained"} onClick={() => inputRef.click()}>
                    Preview
                </Button>
                <Button type={"primary"} variant={"contained"} onClick={deleteImage}>
                    Delete
                </Button>
                <Button type={"primary"} variant={"contained"} onClick={sendToServer}>
                    Upload
                </Button>
            </div>
        </div>
    )
}