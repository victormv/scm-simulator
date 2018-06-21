package hibernate.util;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * 
 * @author Victor Mesquita Viegas
 *
 */

public class JpaUtil 
{
	/**
	 * Atributo que será associado a propriedade 'persistence-unit',
	 * encontrada no arquivo persistence.xml, e utilizado sobre o
	 * método static logo abaixo.
	 */
	private static final String PERSISTENCE_UNIT_NAME = "jpa";
	
	
	/**
	 * Atributo descrito como Fábrica de EntityManager. 
	 * Será responsável por fornecer, quando solicitado, uma 
	 * instância de um EntityManager disponível para a comunicação
	 * com a camada de persistência.
	 */
    private static EntityManagerFactory emf;
    
    /**
     * Binding (associação) automática da Classe JpaUtil com a
     * unidade de persistência (persistence-unit) especificada no
     * arquivo persistence.xml do projeto. Será instânciado
     * uma fábrica de EntityManager.
     */
    static 
    {
        try 
        {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } 
        catch (Throwable ex) 
        {
        	ex.printStackTrace();
        }
    }
    
    /**
     * Método responsável por instânciar um EntityManager,
     * que é fornecido pela Fábrica de EntityManager
     * (EntityManagerFactory), que permitirá o acesso e comunicação
     * a camada de persistência.
     * @return 
     */
    public static EntityManager createEntityManager() 
    {
    	if (!emf.isOpen())
    	{
    		throw new RuntimeException("EntityManagerFactory is closed");
    	}
    	return emf.createEntityManager();
    }
    
    /**
     * Método responsável por finalizar a fábrica de EntityManager. 
     * Após finalizado, será impossibilitado qualquer solicitação
     * de instância de um EntityManager para acesso a 
     * camada de persistência.
     */
    public static void closeEntityManagerFactory() 
    {
    	if (emf.isOpen()) 
    	{
    		emf.close();
    	}
	}
}
