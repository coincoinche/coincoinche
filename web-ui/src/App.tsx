import React from 'react';
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import './App.css';
import MainMenu from "./pages/MainMenu/MainMenu";
import './App.css';
import Ladder from "./pages/Ladder/Ladder";
import Lobby from "./pages/Lobby";
import EndGameScreen from "./pages/MainGame/EndGameScreen";

const App: React.FC = () => {
  return (
    <div className="App">
      <Router>
          <Switch>
            <Route
              path='/game/end'
              render={(props) => <EndGameScreen {...props} />}
            />
            <Route path="/game">
              <Lobby />
            </Route>
            <Route path="/ladder">
              <Ladder />
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
