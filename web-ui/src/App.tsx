import React from 'react';
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import './App.css';
import MainMenu from "./MainMenu/MainMenu";
import './App.css';
import MainGameScreen from "./pages/MainGame/MainGameScreen";
import Ladder from "./pages/Ladder/Ladder";
import TestWebSocket from "./components/websocket/TestWebSocket";

const App: React.FC = () => {
  return (
    <div className="App">
      <Router>
          <Switch>
            <Route path="/game">
              <MainGameScreen />
            </Route>
            <Route path="/ladder">
              <Ladder />
            </Route>
            <Route path="/">
              <MainMenu />
            </Route>
          </Switch>
      </Router>
      <TestWebSocket />
    </div>
  );
};

export default App;
