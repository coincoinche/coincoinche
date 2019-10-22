import React, { Component, FormEvent } from 'react';
import ReactTable from 'react-table';
import 'react-table/react-table.css';
import './Ladder.css';

type LadderState = {
  searchedUser : string;
}

export default class Ladder extends Component<{}, LadderState> {
  constructor(props : any) {
    super(props);
    this.state = {searchedUser: ''};

    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(event: FormEvent<HTMLInputElement>) {
    this.setState({searchedUser: event.currentTarget.value});
  }

  render() {
    const data = [{
      username: 'Tanner Linsley',
      score: 26,
      rank: 1
    }]
    const filteredData = data.filter(u => u.username.toLowerCase().includes(this.state.searchedUser));
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
    }]

    return (
      <div>
        <div className='title'>
          User ladder
        </div>
        <div className='container'>
          <div className='table'>
            <ReactTable
              data={filteredData}
              columns={columns}
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
