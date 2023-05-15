import {legacy_createStore} from "redux";
import rootReducer from "./reducers";
import {createStoreHook} from "react-redux";

const store = legacy_createStore(rootReducer);
export default store;