export const login = (member) => ({type: "LOGIN"});
export const logout = () => ({type: "LOGOUT"});

const initialState = {
    memberName: "",
    memberId: "",
    isLogin: null,
};

export default function MemberData(state = initialState, action) {
    switch (action.type) {
        case "LOGIN":
            return { ...state, isLogin: true, memberId: action.payload.id, memberName: action.payload.name};
        default:
            return state;
    }
}