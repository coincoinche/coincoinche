import React from 'react';
import logo from './assets/coincoinche_logo.png';
import './App.css';
import TestComponent from "./TestComponent";
import MainMenu from "./MainMenu/MainMenu";
import './App.css';
import TestWebSocket from "./websocket/TestWebSocket";

const App: React.FC = () => {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <TestComponent />
        <TestWebSocket />
      </header>
      <MainMenu />
    </div>
  );
};

export default App;
