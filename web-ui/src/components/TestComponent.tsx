import * as React from "react";
import axios from "axios";
import { API_BASE_URL } from '../constants';

type State = {
  data: string;
  loading: boolean;
  error: boolean;
}

export default class TestComponent extends React.Component<{}, State> {
  state: Readonly<State> = {
    data: '',
    loading: true,
    error: false,
  };

  async fetchData() {
    try {
      const response = await axios.get(`${API_BASE_URL}/greetings`);
      return response.data;
    } catch (error) {
      this.setState({
        loading: false,
        error: true,
      })
    }
  }


  componentDidMount() {
    this.fetchData().then(data => {
      this.setState({
        data,
        loading: false,
      })
    });
  }

  render() {
    return <p>
      Message from API: {this.state.loading ? 'Incoming...' : this.state.error ? 'An error occurred' : this.state.data}
    </p>;
  }
}