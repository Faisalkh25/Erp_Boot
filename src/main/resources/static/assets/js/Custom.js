// ---------------------------------------
//  ADMIN PAGE TOGGLE BUTTON JS START HERE
// ---------------------------------------

 const toggleBtn = document.getElementById('toggleBtn');
    const textOut = document.getElementById('textOut');
    const textIn = document.getElementById('textIn');

    let isIn = false;

    toggleBtn.addEventListener('click', () => {
      isIn = !isIn;

      if (isIn) {
        textOut.classList.remove('active');
        textIn.classList.add('active');
        toggleBtn.classList.add('in');
      } else {
        textIn.classList.remove('active');
        textOut.classList.add('active');
        toggleBtn.classList.remove('in');
      }
    });

// -------------------------------------
//  ADMIN PAGE TOGGLE BUTTON JS END HERE
// -------------------------------------


const cursorDot = document.querySelector('.cursor-dot');
const cursorOutline = document.querySelector('.cursor-outline');

let mouseX = 0, mouseY = 0;
let outlineX = 0, outlineY = 0;

// Update mouse coordinates
document.addEventListener('mousemove', e => {
  mouseX = e.clientX;
  mouseY = e.clientY;
  cursorDot.style.left = `${mouseX}px`;
  cursorDot.style.top = `${mouseY}px`;
});

// Animate outer ring with delay
function animate() {
  outlineX += (mouseX - outlineX) / 8;
  outlineY += (mouseY - outlineY) / 8;

  cursorOutline.style.left = `${outlineX}px`;
  cursorOutline.style.top = `${outlineY}px`;

  requestAnimationFrame(animate);
}

animate();
