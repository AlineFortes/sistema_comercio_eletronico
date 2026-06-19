package com.ecommerce;

import com.ecommerce.enums.CategoriaProduto;
import com.ecommerce.enums.StatusPedido;
import com.ecommerce.enums.TipoUsuario;
import com.ecommerce.exception.*;
import com.ecommerce.model.*;
import com.ecommerce.security.Seguranca;
import com.ecommerce.util.FormatoUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitarios do sistema de e-commerce.
 * Demonstra testes dos conceitos de POO.
 */
public class TesteSistema {

    @Test
    public void testeEncapsulamentoProduto() throws ValidacaoException {
        Produto p = new Produto(1, "Notebook", "Notebook Dell", 50000.0, 
            CategoriaProduto.INFORMATICA, 10);
        assertEquals("Notebook", p.getNome());
        assertEquals(50000.0, p.getPreco());
        assertEquals(10, p.getQuantidadeEstoque());
    }

    @Test
    public void testeValidacaoProdutoPrecoInvalido() {
        assertThrows(ValidacaoException.class, () -> {
            new Produto(1, "Teste", "Descricao teste", -10.0, CategoriaProduto.ELETRONICOS, 5);
        });
    }

    @Test
    public void testeHerancaUsuario() throws ValidacaoException {
        Usuario u = new Usuario(1, "Joao", "joao@teste.cv", "senha123", 
            "Mindelo", "+23891234567", TipoUsuario.CLIENTE);
        assertEquals("Cliente", u.getTipo());
        assertTrue(u.isAtivo());
    }

    @Test
    public void testeSegurancaSenha() {
        String senha = "minhaSenha123";
        String hash = Seguranca.hashSenha(senha);
        assertNotEquals(senha, hash);
        assertTrue(Seguranca.verificarSenha(senha, hash));
        assertFalse(Seguranca.verificarSenha("senhaErrada", hash));
    }

    @Test
    public void testeCarrinhoComposicao() throws ValidacaoException, EstoqueInsuficienteException {
        Produto p1 = new Produto(1, "Mouse", "Mouse USB", 1500.0, 
            CategoriaProduto.INFORMATICA, 20);
        Produto p2 = new Produto(2, "Teclado", "Teclado USB", 2500.0, 
            CategoriaProduto.INFORMATICA, 15);

        Carrinho c = new Carrinho();
        c.adicionarItem(p1, 2);
        c.adicionarItem(p2, 1);

        assertEquals(2, c.getItens().size());
        assertEquals(5500.0, c.getTotal(), 0.01);
    }

    @Test
    public void testeCarrinhoVazioException() {
        Carrinho c = new Carrinho();
        assertThrows(CarrinhoVazioException.class, c::validarParaFinalizar);
    }

    @Test
    public void testeEstoqueInsuficiente() throws ValidacaoException {
        Produto p = new Produto(1, "Teste", "Desc", 100.0, CategoriaProduto.ELETRONICOS, 5);
        assertThrows(EstoqueInsuficienteException.class, () -> {
            p.reduzirEstoque(10);
        });
    }

    @Test
    public void testeItemPedidoImutavel() throws ValidacaoException, EstoqueInsuficienteException {
        Produto p = new Produto(1, "Monitor", "Monitor 24", 15000.0, 
            CategoriaProduto.INFORMATICA, 10);
        ItemCarrinho ic = new ItemCarrinho(1, p, 2);
        ItemPedido ip = new ItemPedido(1, 1, ic);

        assertEquals("Monitor", ip.getNomeProduto());
        assertEquals(15000.0, ip.getPrecoUnitario());
        assertEquals(30000.0, ip.getSubtotal(), 0.01);
    }

    @Test
    public void testePolimorfismoPessoa() throws ValidacaoException {
        Pessoa p = new Usuario(1, "Maria", "maria@teste.cv", "senha123",
            "Mindelo", "+23891234567", TipoUsuario.ADMINISTRADOR);
        assertEquals("Administrador", p.getTipo());
    }

    @Test
    public void testeStatusPedido() {
        assertEquals("Pendente", StatusPedido.PENDENTE.getDescricao());
        assertEquals("Entregue", StatusPedido.ENTREGUE.getDescricao());
    }

    @Test
    public void testeFormatoMoeda() {
        assertTrue(FormatoUtil.formatarMoeda(1234.56).contains("1.234,56"));
    }
}