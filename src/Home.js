import React from "react";

export default function Home(){
    return (
        <div>
            <nav className="navbar navbar-default">
                <div className="navbar-header">
                    <button type="button" className="navbar-toggle collapsed"
                            data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
                            aria-expanded="false">
                        <span className="icon-bar"></span>
                        <span className="icon-bar"></span>
                        <span className="icon-bar"></span>
                    </button>
                    <h1>게시판 웹 사이트</h1>
                </div>
                <div className="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul className="nav navbar-nav">
                        <li className="active"><Link to="/">메인</Link></li>
                        <li><Link to="/bbs">게시판</Link></li>
                    </ul>
                    <ul className="nav navbar-nav navbar-right">
                        <li className="dropdown">
                            <Link to={"/"} className="dropdown-toggle"
                               data-toggle="dropdown" role="button" aria-haspopup="true"
                               aria-expanded="false">접속하기<span className="caret"></span></Link>
                            <ul className="dropdown-menu">
                                <li><Link to="/members/login">로그인</Link></li>
                                <li><a href="/members/join">회원가입</a></li>
                            </ul>
                        </li>
                    </ul>
                    <ul th:unless="${#strings.isEmpty(member)}" className="nav navbar-nav navbar-right">
                        <li className="dropdown">
                            <a href="#" className="dropdown-toggle"
                               data-toggle="dropdown" role="button" aria-haspopup="true"
                               aria-expanded="false">회원관리<span className="caret"></span></a>
                            <ul className="dropdown-menu">
                                <li><a href="/members/logout">로그아웃</a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </nav>
            <div className="container">
                <div className="jumbotron">
                    <div className="container">
                        <h1>웹 사이트 소개</h1>
                        <p>이 웹사이트는 스프링부트를 사용하여 만든 웹 사이트입니다. 최소한의 간단한 로직만을 이용하여 개발했습니다. 디자인 플랫폼으로는 부트스트랩을 사용하였습니다.</p>
                        <p><a className="btn btn-primary btn-pull" href="#" role="button">자세히 알아보기></a></p>
                    </div>
                </div>
            </div>
            <div className="container">
                <div id="myCarousel" className="carousel slide" data-ride="carousel">
                    <ol className="carousel-indicators">
                        <li data-target="#myCarousel" data-slide-to="0" className="active"></li>
                        <li data-target="#myCarousel" data-slide-to="1"></li>
                        <li data-target="#myCarousel" data-slide-to="2"></li>
                    </ol>
                    <div className="carousel-inner">
                        <div className="item active">
                            <img th:src="@{images/1.jpg}"/>
                        </div>
                        <div className="item">
                            <img th:src="@{images/2.jpg}"/>
                        </div>
                        <div className="item">
                            <img th:src="@{images/3.jpg}"/>
                        </div>
                    </div>
                    <a className="left carousel-control" href="#myCarousel" data-slide="prev">
                        <span className="glyphicon glyphicon-chevron-left"></span>
                    </a>
                    <a className="right carousel-control" href="#myCarousel" data-slide="next">
                        <span className="glyphicon glyphicon-chevron-right"></span>
                    </a>
                </div>
            </div>
        </div>
    )
}