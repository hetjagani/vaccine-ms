import React from 'react';
import Table from 'react-bootstrap/Table';
import AdminNavbar from './AdminNavbar';
import Button from 'react-bootstrap/Button'
export default function Clinic() {
  return (
    <div>
    <AdminNavbar/>
      <Table striped bordered hover size="sm">
        <thead>
          <tr>
            <th>Clinic Name</th>
            <th>Description</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>Coivd-19</td>
            <td>jaefhd vhds vhsd vds jdk vhd hd vjh</td>
            <td><Button variant="primary">Update</Button></td>
            <td><Button variant="danger">Delete</Button></td>
          </tr>
          <tr>
            <td>Dengue</td>
            <td>Bhbh jd kjd dc hsd </td>
          </tr>
        </tbody>
      </Table>
    </div>
  );
}
