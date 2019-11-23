import React from 'react';
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import './App.css';
import MainMenu from "./pages/MainMenu/MainMenu";
import './App.css';
import Ladder from "./pages/Ladder/Ladder";
import Lobby from "./pages/Lobby";
import Login from "./pages/UserAuthentication/Login/Login";
import Logout from "./pages/UserAuthentication/Logout/Logout";
import SignUp from "./pages/UserAuthentication/SignUp/SignUp";

const App: React.FC = () => {
  return (
    <div className="App">
      <Router>
        <Switch>
          <Route path="/game">
            <Lobby />
          </Route>
          <Route path="/ladder">
            <Ladder />
          </Route>
          <Route path="/login">
            <Login />
          </Route>
          <Route path="/logout">
            <Logout />
          </Route>
          <Route path="/signup">
            <SignUp />
          </Route>
          <Route path="/">
            <MainMenu />
          </Route>
        </Switch>
      </Router>
    </div>
  );
};

export default App;
