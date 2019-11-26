import React, { Component, FormEvent } from 'react';
import ReactTable from 'react-table';
import 'react-table/react-table.css';
import './Ladder.css';
import Title from "../../components/misc/Title";
import Button from "../../components/misc/Button";
import {API_BASE_URL} from "../../constants";

type LadderState = {
  searchedUser : string;
  data: any;
}

export default class Ladder extends Component<{}, LadderState> {
  constructor(props : any) {
    super(props);
    this.state = {
      searchedUser: '',
      data: []
    };

    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(event: FormEvent<HTMLInputElement>) {
    this.setState({searchedUser: event.currentTarget.value});
  }

  componentDidMount(): void {
    fetch(`${API_BASE_URL}/ladder`)
      .then(res => res.json())
      .then(res => {
        const data = res.map((row: any, index: number) => ({
          username: row.username,
          score: row.rating,
          rank: index + 1,
        }));
        this.setState({data})
      })
  }

  render() {
    const filteredData = this.state.data.filter((u: any) => u.username.toLowerCase().includes(this.state.searchedUser));
    const columns = [{
      Header: (props : any) => <span className='columnName'>Rank</span>,
      accessor : 'rank',
      Cell: (props : any) => <span className='columnItem'>{props.value}</span>
    }, {
      Header: (props : any) => <span className='columnName'>User</span>,
      accessor: 'username',
      Cell: (props : any) => <span className='columnItem'>{props.value}</span>
    }, {
      Header: (props : any) => <span className='columnName'>Score</span>,
      accessor: 'score',
      Cell: (props : any) => <span className='columnItem'>{props.value}</span>
    }];

    return (
      <div>
        <Title fontSize={70}>User ladder</Title>
        <div className='container'>
          <div className='table'>
            <ReactTable
              data={filteredData}
              columns={columns}
              NextComponent={Button}
              PreviousComponent={Button}
              nextText="Next"
              previousText="Previous"
            />
          </div>
          <div className='research'>
            <label>Search User</label>
            <input className='researchInput' onChange={this.handleChange}/>
          </div>
        </div>
      </div>
    )
  }
}
