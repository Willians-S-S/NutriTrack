import Button from '../../components/Button/Button'

function testef() {
  console.log('a')
}

function Home() {
    return (
      <>
          <h1>Login</h1>
          <Button title='Login' onClick={testef}/>
      </>
    );
  }
  
  export default Home;
  